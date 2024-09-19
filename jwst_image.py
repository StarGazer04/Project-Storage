import os
import pandas as pd
import matplotlib.pyplot as plt
from azure.storage.blob import BlobServiceClient, BlobClient
from astropy.io import fits
from astroquery.mast import Observations
import numpy as np
import matplotlib.pyplot as plt
import time
from astropy.visualization import astropy_mpl_style

from astropy.table import Table

print("Starting script...")
connecting_str = os.getenv('AZURE_JWST_CONNECTION_STRING')
container_name = 'jwstselecteddata'
output_container = 'jwstoutputdata'
used_container = 'jwstdataused'
service_client = BlobServiceClient.from_connection_string(connecting_str)
container_client = service_client.get_container_client(container_name)


# move_blob_to_used_container will take data from jwstselectdata (2nd step in pipeline) and will transfer it to used data. data from jwstobjectdata will then be deleted as to not be used again
def move_blob_to_used_container(blob_service_client, source_container_name, used_container_name, blob_name):
    try:
        source_blob_client = blob_service_client.get_blob_client(container = 'jwstselecteddata', blob = blob_name)
        used_blob_client = blob_service_client.get_blob_client(container= 'jwstdataused', blob = blob_name)
        blob_data = source_blob_client.download_blob().readall()
        used_blob_client.upload_blob(blob_data, overwrite = True)
        print(f"Data for {blob_name} uploaded to '{used_container_name}' successfully.")
        source_blob_client.delete_blob()
        print(f"{blob_name} deleted from '{source_container_name}'.")
    except Exception as e:
        print(f"An error occurred: {e}")

print("w")
print("w")
print("w")
print("w")
print("w")
print("w")

def process_csv(file_name, all_downloaded_files, plot_name):
    print("Starting script...")
    

    # BEGING searching through data to get obsid id, and use obsid id to calibrate data for further lookup
    try:
        blob_client = container_client.get_blob_client(blob=file_name)
        local_path = file_name
        with open(local_path, "wb") as download_file:
            download_data = blob_client.download_blob().readall()
            download_file.write(download_data)
        
        download_file.close()
        data_frame = pd.read_csv(local_path)
        jwst_data = data_frame[data_frame['obs_collection'] == 'JWST']
        print(jwst_data)
        print("on nircam")
        nircam_data = jwst_data[jwst_data['instrument_name'] == 'NIRCAM/IMAGE']
        print(nircam_data)
        first_obsid = nircam_data.iloc[0]['obsid']
        print(first_obsid)
        data_products = Observations.get_product_list(f'{first_obsid}')
        print(f'{data_products} : \n')
        calibrate = data_products[(data_products['calib_level'] >=3)]
        product_sub = calibrate[(calibrate["productSubGroupDescription"]=='I2D')]
        print(product_sub)
        print('wwwww')
        if len(product_sub) > 0:
                print(f'Downloading type: I2D')
                manifest = Observations.download_products(product_sub, mrp_only=True)
                print(manifest)
                print('w')
                manifest
                for file_info in manifest['Local Path']:
                    all_downloaded_files.append(file_info)
                    hdulist = fits.open(file_info)
                    hdulist.info()  # Optionally print info about the FITS file
                    data = hdulist[1].data  # Assuming the data you need is in the first extension
                    plt.figure()
                    plt.imshow(data, cmap='gray', interpolation='nearest', vmin=0, vmax=45)
                    plt.colorbar()
                    
                    plot_path = plot_name.replace('.txt', '.png')
                    plt.savefig(plot_path, format = 'png')
                    blob_client = service_client.get_blob_client(container = output_container, blob = plot_path)
                    with open(plot_path, 'rb') as data:
                        blob_client.upload_blob(data, overwrite = True)
                        print(f"Uploaded plot to Azure Blob Storage: {plot_path}")
                    plt.show()
                    plt.close()
                    os.remove(plot_path)
                    break
        else:
            print('data does not claibrate with I2D')

    except Exception as e:
        print(f"An error occurred: {e}")
max = 1
count = 0
all_downloaded_files = [] 
blob_list = container_client.list_blobs()
for blob in blob_list:
    if count >= max:
        break
    #begin call to move_blob_to_used_container
    process_csv(blob.name, all_downloaded_files,blob.name)
    count +=1
    if blob.name.endswith('txt'):
        move_blob_to_used_container(service_client, container_name, used_container, blob.name)

for file_path in all_downloaded_files:
    try:
        os.remove(file_path)
        print(f"Successfully deleted FITS file: {file_path}")
    except Exception as e:
        print(f"Failed to delete FITS file {file_path}: {e}")

time.sleep(30)










    