### ListMerger

Personal project to merge all sorts of hit lists (e.g. big lists such as NPO Radio 2 Top 2000, Radio 10 Top 4000, Veronica Top 1000)

For instance merging

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<top2000database2014>
<record>
    <Artiest>AC/DC</Artiest>
    <Nummer>Highway To Hell</Nummer>
    <R2NLA2019>20</R2NLA2019>
</record>
<record>
    <Artiest>Rolling Stones</Artiest>
    <Nummer>Angie</Nummer>
    <R2NLA2019>10</R2NLA2019>
</record>
</top2000database2014>
```

with

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<top2000database2014>
<record>
    <Artiest>Rolling Stones</Artiest>
    <Nummer>Angie</Nummer>
    <VA2019>50</VA2019>
</record>
<record>
    <Artiest>ZZ Top</Artiest>
    <Nummer>Sharp Dressed Man</Nummer>
    <VA2019>60</VA2019>
</record>
</top2000database2014>
```

would yield:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<top2000database2014>
<record>
    <Artiest>AC/DC</Artiest>
    <Nummer>Highway To Hell</Nummer>
    <R2NLA2019>20</R2NLA2019>
</record>
<record>
    <Artiest>Rolling Stones</Artiest>
    <Nummer>Angie</Nummer>
    <R2NLA2019>10</R2NLA2019>
    <VA2019>50</VA2019>
</record>
<record>
    <Artiest>ZZ Top</Artiest>
    <Nummer>Sharp Dressed Man</Nummer>
    <VA2019>60</VA2019>
</record>
</top2000database2014>
```
<br></br><br></br>
### Versions

1.0     STABLE ORIGINAL VERSION  
1.1     MOVED WRITER.JAVA, ADDED RUDIMENTARY TEST  
1.2     ADDED RECORDCLEANER.JAVA, BUGFIXES  
2.0     CAN NOW DIRECTLY PARSE EXCEL FILES  
2.1     CLEANED UP STARTMERGING.JAVA  
2.2     ADDED LIST.EXCEPTIONS TO IGNORE XML FILES  
2.3     MADE SYSTEM OUTS NICER, IMPROVED EXCEL HANDLING  
3.1     NO LONGER USING RECORDCLEANER/SONGEXCEPTIONS  
3.2     ADDED ADDITIONAL INFO COLUMNS, MAJOR CODE IMPROVEMENTS  
3.3     MADE MODULE SONARLINT COMPLIANT, ADVANCED FILTERING  
3.4     ADDED MULTI EXCEL SHEET PARSING, ADDED HANDLING MULTIPLE RESOURCE FOLDERS