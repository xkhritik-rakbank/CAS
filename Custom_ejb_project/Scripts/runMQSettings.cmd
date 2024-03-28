@REM Copyright (c) 2004 NEWGEN All Rights Reserved.

@REM ************************************************************************************************
@REM Modify these variables to match your environment
	cls
	set JAVA_HOME="C:\Program Files (x86)\Java\jdk1.6.0_29"
	set JTS_LIBPATH=..\lib
	set MYCLASSPATH=..\classes
	set LIBCLASSPATH=%JTS_LIBPATH%\omnishared.jar;%JTS_LIBPATH%\ejb.jar;%JTS_LIBPATH%\WFSShared.jar;%JTS_LIBPATH%\omnidocs_hook.jar;%JTS_LIBPATH%\com.ibm.mq.jar;%JTS_LIBPATH%\xom.jar;%JTS_LIBPATH%\log4j-1.2.14.jar;%JTS_LIBPATH%\msbase.jar;%JTS_LIBPATH%\mssqlserver.jar;%JTS_LIBPATH%\msutil.jar
@REM ************************************************************************************************

@REM ************************************************************************************************
@REM Compile WFCustom
	cd ..\src
	%JAVA_HOME%\bin\java -classpath .;%LIBCLASSPATH%;%MYCLASSPATH% com.newgen.omni.jts.txn.cust.MQHelper
	pause
@REM ************************************************************************************************