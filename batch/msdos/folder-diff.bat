cd %~dp0

SET old=..\..\..\base64-without-bitwise-operators-old\
SET new=..\..\..\base64-without-bitwise-operators\

diff -x target -x folder-diff.path -x .lein-failures -x .lein-repl-history -x .nrepl-port -x .project -x *.log -ruN %old% %new% > folder-diff.path
pause
