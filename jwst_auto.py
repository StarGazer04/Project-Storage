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
select_container = 'jwstselecteddata'
output_container = 'jwstoutputdata'
used_container = 'jwstdataused'
calibrate_container = 'jwstcalibrateddata'
service_client = BlobServiceClient.from_connection_string(connecting_str)
container_client = service_client.get_container_client(calibrate_container)

def process_csv(file_name):
    try:
        # Download CSV file from Azure Blob Storage
        blob_client = container_client.get_blob_client(blob=file_name)
        download_file_path = f"temp_{file_name}"
        with open(download_file_path, "wb") as download_file:
            download_data = blob_client.download_blob().readall()
            download_file.write(download_data)
        
        # Convert CSV to DataFrame to Astropy Table
        df = pd.read_csv(download_file_path)
        astropy_table = Table.from_pandas(df)
        
        # Save Astropy Table to FITS file
        fits_file_path = download_file_path.replace('.txt', '.fits')
        astropy_table.write(fits_file_path, format='fits', overwrite=True)
        print(fits_file_path)
        
        with fits.open(fits_file_path) as hdul:
            print("HDU List Information:")
            hdul.info()  # Display information about all HDUs

            # Assuming the table with your data is in the first HDU (index 1)
            data_table = hdul[1].data  # This is the table data

            # Filter rows where 'productSubGroupDescription' equals 'I2D'
            # This creates a mask that is True for rows where the condition is met
            mask = data_table['productSubGroupDescription'] == 'I2D'
            filtered_data = data_table[mask]

            # Display filtered data
            print("Filtered Data:")
            for row in filtered_data:
                print(row)

           


        # Use the extracted data to download products
        download_url = filtered_data["productFilename"]
        print(download_url)
        with fits.open(download_url) as url:
            url.info()
    except Exception as e:
        print(f"An error occurred processing {file_name}: {e}")

max = 1
count = 0
all_downloaded_files = [] 
blob_list = container_client.list_blobs()
for blob in blob_list:
    if count >= max:
        break
    #begin call to move_blob_to_used_container
    process_csv(blob.name)
    count +=1
    

for file_path in all_downloaded_files:
    try:
        os.remove(file_path)
        print(f"Successfully deleted FITS file: {file_path}")
    except Exception as e:
        print(f"Failed to delete FITS file {file_path}: {e}")

time.sleep(30)










    