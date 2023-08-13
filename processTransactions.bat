setlocal
set fileName="%cd%\%~1"
set balanceSheet="%cd%\%~2"

java -jar %~dp0target\bookkeeping-tool-jar-with-dependencies.jar %fileName% %balanceSheet%

endlocal
