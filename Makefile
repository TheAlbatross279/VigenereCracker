JCC = javac

%.class : %.java
	$(JCC) $<

CLASSES = \
	kasiski.class \
	ftable.class \
	cipherio.class \
	ic.class

world: clean $(CLASSES)

clean: 
	rm -f $(CLASSES)


