AWS S3 with Spring Boot
-----------------------

Introduction
------------
This is a demo of Spring Boot uploading a file to AWS S3.

Upload postcodes files to postcodelocationfinderfiles bucket containing list of Postcodes to display.

The postcodelocationfinderfiles bucket has PostcodeLocationFinderFileEvent Event Notification configured to sent SQS message to 
PostcodeLocationFilesQueue where Spring Boot PostcodeLocationFinder listens for messages.

 
Two Options:
1. The postcodelocationstorefiles bucket has PostcodeLocationStoreFileEventThe postcodelocationstorefiles bucket has PostcodeLocationStoreFileEvent Event Notification configured to send Lambda message to PostcodeLocationStoreLambda Lambda Function.

2. The PostcodeLocationStoreLambda Lambda Function has an S3 Event Source on postocdelocationstorefiles Object Created (all) Event
for csv files.


AWS Credentials
---------------
Add when running locally to pick up Profile Credentials Provider - this will use the credentials in $USER_HOME/.aws/credentials
-Duse.profile.credentials=true

Deployment to Elasticbeanstalk as a Docker Image.
-------------------------------------------------

Need to add the following Role Policies to the Elasticbeanstalk Image Role:
- AmazonS3FullAccess

Need to add a S3:
Create S3 Bucket - postcodebucket

Postcode S3 URLs
----------------
Upload file to S3:
http://localhost:8080/health

http://localhost:8080/upload?bucket=postcodelocationfinderfiles&key=postcodes.csv&filename=E:/dev/git/Postcode-S3/testData/postcodes.csv

http://localhost:8080/upload?bucket=postcodelocationstorefiles&key=SMpostcodes.csv&filename=E:/dev/data/area/SMpostcodes.csv

Example URLs after deployment to AWS
------------------------------------
http://postcodelocations3.elasticbeanstalk.com/health

http://postcodelocations3.elasticbeanstalk.com/upload?bucket=postcodelocationfinderfiles&key=postcodes.csv&filename=E:/dev/git/uk.co.keithj.postcodes3/testData/postcodes.csv

Gradle Build
------------

gradlew buildDockerZip

Deployment to AWS
-----------------
Uses eb CLI to deploy:

eb-create-and-deploy.cmd
eb-terminate-all.cmd


TODO
----
1. Make work on AWS in Docker - this will need to upload the file to AWS and then store from memory.
(Only tested uploading file from disk location on Local Computer)

2. Implement Retrieve back using RESTful interface to download file from s3.

