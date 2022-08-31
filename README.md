# DataBeautifier
Processes XML and XSL files into PDF files and more

### Purpose
DataBeautifier is simply an XSL processor written in java that also utilizes PDFBox libraries and JFreeChart to generate PDFs and Charts.

To execute the jar file create a shell script to call the jar file after processes have run that generate the XML and XSL files needed.

To execute via command line, use something like the following:

    java -jar $DIRECTORY/DataBeautifier.jar "$TO_TYPE" ${DIRECTORY}${XSLFILE} ${DIRECTORY}${XMLFILE} "$LOG"

#### TO_TYPE
- CSV
- PDF
- PDFPORTRAIT
- XLS
- or more depending on how you layout your XSL file

#### XSLFILE
- This is the filename of the XSL file, make sure to also pass in the directory where it's located

#### XMLFILE
- This is the filename of the XML file, make sure to also pass in the directoyr where it's located

#### LOG
- This is the filename of the LOG file that gets written to as DataBeautifier is working

By default, PDFs output in landscape orientation, use PDFPORTRAIT as the TO_TYPE to output in portrait orientation

Check the /input/ directory for an example XML and XSL file that can be used to generate a PDF

## Charts
Charts can also be generated without XSL, by outputting specific XML in a specific way.  See an example under /input/

All charts must be listed in the xml with <chart></chart> tags

Within the <options></options> tag you specific what kind of chart you're wanting to generate.

Here are the tags necessary under options and an example below

    <options>
        <type>BAR</type>
        <title>This is the Title</title>
        <categoryLabel>These are the Categories</categoryLabel>
        <valueLabel>These are the Values</valueLabel>
        <orientation>VERTICAL</orientation>
        <showLegend>true</showLegend>
        <showTooltips>true</showTooltips>
        <width>1024</width>
        <height>480</height>
    </options>

#### type
Some types available are:
- PIE
- BAR
- LINE
- AREA

#### title
The title of the chart

#### categoryLabel
Label used to define categories

#### valueLabel
Label used to define values

#### orientation
- VERTICAL
- HORIZONTAL

#### showLegend
- true
- false

#### showTooltips
- true
- false

#### width
The width of the chart in pixels

#### height
The height of the chart in pixels

For the data values that the chart utilizes, you need to build the XML with multiple <rec> tags for each value.  An example is given below

    <rec>
        <category>test</category>
        <name>name1</name>
        <value>10</value>
    </rec>
