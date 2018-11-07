:; echo "running API in Mac"
:; DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null && pwd )"
:; osascript -e "tell application \"Terminal\" to do script \"cd '$DIR';chmod u+x runApi2;./runApi2\""

echo "running API in Windows"
set mypath=%cd%
echo %mypath%
start cmd /k runApi2.bat
pause
