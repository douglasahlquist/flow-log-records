The requirements have been added to and altered because of issues.

Note: There is a revision to this project as was discovered ealier according to the poorly documented AWS Flow Log Records, Field 8 in the CSV refers the protocol, but flowing the url anchor tag refers to the PROTOCOL NUMBER (within the IANA linked document) or in the Assigned Internet Protocol Numbers in the AWS Document.   The set me off making some big assumptions, not connecting 'PROTOCOL NUMBER' with port number (as an integer).  I had started some of this work with out internet access or a working data plan.  Working from Starbuck did help either (no internet there either.  Todat Sunday March 30, I fly to Las Vegas to pick up my truck that was left there weeks before to make it home to a cancel in person interview that was cancel during my return flight home.  Given that my approach made some fatal assumptions that where cleared up far too late.
These files would have change my direction earlier:
AWS_Flow_Control_Logs.txt
Flow_Log_Records/iana.csv
PROTOCOLS1.txt

With data in the last checkin prior to thisthere is better informationtoi have a better completion.

Issues:
1.  The first issue is devired from real world condions.  In looking at communication protocols there are to major types:
	a. TCPIP
	b. UDP
The problem comes in the lookup of the port from the Flow Log Records.  Only the PORT number is refference in the FLOW Log, which the Wiki refers to https://en.wikipedia.org/wiki/List_of_TCP_and_UDP_port_numbers
In some cases the port itself has implementations have both TCP and UDP listeners.  Here the requirement do not account for situations where one and not the other.   Because of this an adaptation was made to do a lookup for both TCP and UDP and note in the count log if this possibility of either could have resulted.  Look at the TagListFromWiki.csv file to notice that in some case both TCP and UDP are both options.  More needs to be added to the specific AWS 'eni' to see what process is running on the port...  but that is sort of the point of this exercise.   

2. Because simple makijng up a port/protocol list up does not meet the real world conditions JAVA code was created to create the required data.  a portion of the date was extracted from the above wiki page.

There are 3 processes create to get to the stated result
1. WikiFileUtils.sh
	reads the TagListFromWiki.csv and creates the tag_list.csv file
	reads 2 input arguments 
		a. the input file (as String) 
		b. the output file (as String)
 
2. CreateFlowLog.sh
	create the Flow Log Record file
	reads 3 input arguments 
		a. the output file from '1' (path as String) 
		b. the number of records to create (as Integer) 
		c. the output filename to write the the Flow Log Records to (as String)

3. GenerateTagMap.sh
	Create the TagCountFile, with sslight variations from the requirements
	reads 3 arguments
		a. protocolKeyInputFile (path as String)
		b. flowLogDataInputFile (path as Strig) 
		c. outputFile (path as String)


All shell scripts are found in the Flow-Log-Records folder
