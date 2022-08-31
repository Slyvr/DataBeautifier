<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
   version="1.0" xmlns="urn:schemas-microsoft-com:office:spreadsheet"
   xmlns:msxsl="urn:schemas-microsoft-com:xslt" xmlns:x="urn:schemas-microsoft-com:office:excel"
   xmlns:ss="urn:schemas-microsoft-com:office:spreadsheet">
   <xsl:output method="xml" indent="yes" />

   <xsl:template match="/">
      <xsl:processing-instruction name="mso-application">
         progid="Excel.Sheet"
      </xsl:processing-instruction>
      <Workbook>
         <ExcelWorkbook xmlns="urn:schemas-microsoft-com:office:excel">
            <WindowHeight>10005</WindowHeight>
            <WindowWidth>10005</WindowWidth>
            <WindowTopX>120</WindowTopX>
            <WindowTopY>135</WindowTopY>
            <ProtectStructure>False</ProtectStructure>
            <ProtectWindows>False</ProtectWindows>
         </ExcelWorkbook>
         <Styles>
            <Style ss:ID="head_cell_style">
               <Font ss:Bold="1" x:Family="Swiss" />
               <Borders>
                  <Border ss:LineStyle="Continuous" ss:Position="Left" ss:Weight="1" />
                  <Border ss:LineStyle="Continuous" ss:Position="Top" ss:Weight="1" />
               </Borders>
               <Interior ss:Color="#C0C0C0" ss:Pattern="Solid" />
               <Alignment ss:Vertical="Bottom" ss:WrapText="1" />
            </Style>
            <Style ss:ID="value_row_style">
               <Borders>
                  <Border ss:LineStyle="Continuous" ss:Position="Left" ss:Weight="1" />
                  <Border ss:LineStyle="Continuous" ss:Position="Right" ss:Weight="1" />
                  <Border ss:LineStyle="Continuous" ss:Position="Top" ss:Weight="1" />
                  <Border ss:LineStyle="Continuous" ss:Position="Bottom" ss:Weight="1" />
               </Borders>
            </Style>
         </Styles>
         <xsl:apply-templates select="xml_main" />
      </Workbook>
   </xsl:template>

   <xsl:template match="xml_main">
      <Worksheet ss:Name="{@name}">
         <Table ss:DefaultRowHeight="13.2" ss:ExpandedColumnCount="5"
            x:FullColumns="1" x:FullRows="1">
            <Column ss:Index="1" ss:Width="100" />
            <Column ss:Index="2" ss:Width="100" />
            <Column ss:Index="3" ss:Width="100" />
            <Column ss:Index="4" ss:Width="100" />
            <Column ss:Index="5" ss:Width="100" />
            <Row>
               <!-- Header Row -->
               <xsl:apply-templates select="person[1]/*" mode="headers" />
            </Row>
            <xsl:for-each select="person">
               <Row>
                  <Cell ss:StyleID="value_row_style">
                     <Data ss:Type="String"><xsl:value-of select="iden" /></Data>
                  </Cell>
                  <Cell ss:StyleID="value_row_style">
                     <Data ss:Type="String"><xsl:value-of select="firstname" /></Data>
                  </Cell>
                  <Cell ss:StyleID="value_row_style">
                     <Data ss:Type="String"><xsl:value-of select="lastname" /></Data>
                  </Cell>
               </Row>
            </xsl:for-each>
         </Table>
      </Worksheet>
   </xsl:template>

   <xsl:template match="person[1]/*" mode="headers">
      <Cell ss:StyleID="head_cell_style">
         <Data ss:Type="String"><xsl:value-of select="name()" /></Data>
      </Cell>
   </xsl:template>
</xsl:stylesheet>