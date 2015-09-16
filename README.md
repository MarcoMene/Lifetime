# Lifetime
To compile:
mvn clean compile assembly:single

To run the analysis:
java -cp <target.jar> Main <installfile.csv> <targetfile.csv> <sessionfile1.csv> <sessionfile2.csv> ..

Then you can read data from R and do whatever you want

Csv file structure:

Installs:
,IDFA,date
1,000C2B0D-376A-485F-9BDE-86C023D29A02,22/06/15
...

Sessions:
,IDFA,timestamp,duration
0,F3F71BE1-31AE-4F0A-962B-2B145DD0797F,1434960613443,18.0
...

Target:
t,p,dp
0,0.9771540588727576,3.679297640532043E-4
...
