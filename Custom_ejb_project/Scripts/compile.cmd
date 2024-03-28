@REM Copyright (c) 2004 NEWGEN All Rights Reserved.

@REM ************************************************************************************************
@REM Modify these variables to match your environment
	cls
	set JAVA_HOME="C:\Program Files\Java\jdk1.8.0_231"
	set JTS_LIBPATH=..\lib
	set MYCLASSPATH=..\classes
	set LIBCLASSPATH=%JTS_LIBPATH%\omnishared.jar;%JTS_LIBPATH%\jbossall-client.jar;%JTS_LIBPATH%\WFSShared.jar;%JTS_LIBPATH%\omnidocs_hook.jar;%JTS_LIBPATH%\com.ibm.mq.jar;%JTS_LIBPATH%\xom.jar;%JTS_LIBPATH%\log4j-1.2.14.jar;%JTS_LIBPATH%\WebService.jar
@REM ************************************************************************************************

@REM ************************************************************************************************
@REM Compile WFCustom
	cd ..\src
	%JAVA_HOME%\bin\javac -d %MYCLASSPATH% -classpath %LIBCLASSPATH%;%MYCLASSPATH% ..\src\com\newgen\omni\jts\txn\cust\*.java
	pause
@REM ************************************************************************************************