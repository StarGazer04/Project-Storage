from astropy.io import fits
from astroquery.mast import Observations
import pandas as pd
import os

# connection to azure. uploads file from query_objets to put into azure.
from azure.storage.blob import BlobServiceClient
def upload_data_to_azure(file_path, container_name, blob_name):
    connecting_str = os.getenv('AZURE_JWST_CONNECTION_STRING')
    if connecting_str is None:
        print("Connection string is not set.")
    else:
        print("Connection string retrieved successfully.")
    block_service_client = BlobServiceClient.from_connection_string(connecting_str)
    blob_client = block_service_client.get_blob_client(container = container_name, blob = blob_name)
    with open(file_path, "rb") as data:
        blob_client.upload_blob(data, overwrite = True)
        print(f"Uploaded {file_path} to Azure Blob Storage")

galaxy_list = ["M31", "M33", "NGC 6822", "IC 10"]
nebula_list = ["M42", "M1", "Barnard 33", "M8", "NGC 2237"]
radius_value = ".02 deg"

container_name = "jwstobjectdata"

# loops to access data from galaxy and nebulae. calls upload_data_to_azure each iteration
def query_objects(object_list, object_type):
    for object_name in object_list:
        filename = f"{object_name}_data.csv"
        try:
            obsByName = Observations.query_object(object_name,radius=radius_value)
            print("Number of results from all missions from: " + object_type + ":",len(obsByName))
            if len(obsByName) > 0:
                obsByName_table = obsByName.to_pandas()
                obsByName_table.to_csv(filename, index = False)
                upload_data_to_azure(filename,container_name, filename)
                print(f"data saved to {filename}")
        except Exception as e:
            print (f"failed to retrieve data for {object_name}: {str(e)}")

query_objects(galaxy_list, "Galaxy")
query_objects(nebula_list, "Nebula")

