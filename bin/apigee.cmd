@echo off
rem Licensed to the Apache Software Foundation (ASF) under one or more
rem contributor license agreements.  See the NOTICE file distributed with
rem this work for additional information regarding copyright ownership.
rem The ASF licenses this file to You under the Apache License, Version 2.0
rem (the "License"); you may not use this file except in compliance with
rem the License.  You may obtain a copy of the License at
rem
rem     http://www.apache.org/licenses/LICENSE-2.0
rem
rem Unless required by applicable law or agreed to in writing, software
rem distributed under the License is distributed on an "AS IS" BASIS,
rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
rem See the License for the specific language governing permissions and
rem limitations under the License.
setlocal

rem assume script is always in /bin directory
FOR %%i IN ("%~dp0..") DO SET "APIGEE_HOME=%%~fi"

rem DEBUG
set APIGEE_LIB="%APIGEE_HOME%\target\lib"
rem set APIGEE_LIB="%APIGEE_HOME%\lib"

call "%APIGEE_HOME%\bin\setclasspath.bat"

echo CP %CLASSPATH%

set CLASSPATH="target\lib\commons-beanutils.jar;target\lib\commons-cli.jar;target\lib\commons-lang3.jar;target\lib\gson.jar;target\lib\ini4j.jar;target\lib\slf4j-api.jar;target\lib\slf4j-simple.jar;target\lib\jcl-over-slf4j.jar;target/classes"

java -cp "%CLASSPATH%" io.apigee.tools.cli.CLIDriver %*
