setlocal
set fileName="%cd%\%~1"

java -jar %~dp0target\bookkeeping-tool-jar-with-dependencies.jar %fileName%

endlocal
