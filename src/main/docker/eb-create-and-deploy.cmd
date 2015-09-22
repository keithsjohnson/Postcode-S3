@REM https://docs.aws.amazon.com/elasticbeanstalk/latest/dg/eb-cli3.html
eb init postcodelocations3 --region eu-west-1 --platform "Docker 1.6.2"

eb create postcodelocations3 --cname postcodelocations3 --instance_type t2.micro --region eu-west-1 --tier webserver --instance_type aws-elasticbeanstalk-ec2-s3-sqs-role --service-role aws-elasticbeanstalk-service-role
