import boto
import sys, os
from boto.s3.key import Key

aws_access_key_id='AKIAISMW6SCS6BI4TTNQ'
aws_secret_access_key='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'
bucket_name='pivotalkorea'
LOCAL_PATH='/tmp/s3'

def createdir(path):
    if not os.path.exists(path):
        os.makedirs(path)

def download(path):
    conn = boto.connect_s3(aws_access_key_id, aws_secret_access_key)
    bucket=conn.get_bucket(bucket_name)
    bucket_list = bucket.list()

    for b in bucket_list:
        keyString = str(b.key)
        print(keyString)
        if keyString.endswith("/"):
           createdir(path+'/'+keyString)
        if not os.path.exists(path+'/'+keyString):
            b.get_contents_to_filename(path+'/'+keyString)



createdir(LOCAL_PATH)

download(LOCAL_PATH)


