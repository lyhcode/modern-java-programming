topdf:
	pandoc --template=default.latex --latex-engine=xelatex -V fontsize=13pt README.md -o README.pdf
	pandoc -t bbcode.lua README.md -o README.bbcode
	pandoc -t ithelp.lua README.md -o README.ithelp
	# pandoc --template=default.latex --latex-engine=xelatex -V fontsize=13pt 01-installation.txt -o 01-installation.pdf
	#zip -r ../ireport.zip part1* part2* part3* author*
