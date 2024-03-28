@REM Copyright (c) 2004 NEWGEN All Rights Reserved.

@REM ************************************************************************************************
@REM Modify these variables to match your environment
	cls
	set JAVA_HOME="C:\Program Files\Java\jdk1.8.0_231"
	set MYCLASSPATH=..\classes
	set JARPATH=..\application
@REM ************************************************************************************************

 	cd %MYCLASSPATH%

@REM WFCustom jar

@REM %JAVA_HOME%\bin\jar -cvf %JARPATH%\wfcustom_ejb.jar com\newgen\omni\jts\txn\*.class
%JAVA_HOME%\bin\jar -uvf %JARPATH%\wfcustom_ejb.jar com\newgen\omni\jts\txn\cust\*.class
%JAVA_HOME%\bin\jar -uvf %JARPATH%\wfcustom_ejb.jar com\newgen\omni\jts\txn\cust\*.xml
%JAVA_HOME%\bin\jar -uvf %JARPATH%\wfcustom_ejb.jar com\newgen\omni\jts\txn\cust\*.properties
@REM %JAVA_HOME%\bin\jar -uvf %JARPATH%\wfcustom_ejb.jar com\newgen\omni\jts\txn\clocal\*.class
@REM %JAVA_HOME%\bin\jar -uvf %JARPATH%\wfcustom_ejb.jar buildversion.txt

@REM %JAVA_HOME%\bin\jar -uvf %JARPATH%\wfcustom_ejb.jar com\newgen\omni\jts\txn\cust\RequestXMLs

@REM ************************************************************************************************

pause;




