all:
	for file in README ; do \
		pandoc --template=default.latex --latex-engine=xelatex -V fontsize=13pt $$file.md -o $$file.pdf ; \
		pandoc -t bbcode.lua $$file.md -o $$file.bbcode ; \
		pandoc -t ithelp.lua $$file.md -o $$file.ithelp ; \
	done
	# pandoc --template=default.latex --latex-engine=xelatex -V fontsize=13pt 01-installation.txt -o 01-installation.pdf
	#zip -r ../ireport.zip part1* part2* part3* author*
