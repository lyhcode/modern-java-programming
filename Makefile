topdf:
	pandoc --template=default.latex --latex-engine=xelatex -V fontsize=13pt 01-installation.txt -o 01-installation.pdf
	#zip -r ../ireport.zip part1* part2* part3* author*
