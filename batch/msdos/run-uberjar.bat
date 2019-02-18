md "%HOMEPATH%\_delete_content\"
pushd %~dp0\..\..
call java -jar .\target\uberjar\base64-without-bitwise-operators-uberjar.jar
pause
popd
