all:
	mkdir -p build
	javac -sourcepath src -d build src/com/rahul/graph/*.java	
	jar -cfm graph.jar MANIFEST.MF -C build/ com/rahul/graph/ -C src/ testFiles/

clean:
	rm -rf build/
	rm -f graph.jar
	
