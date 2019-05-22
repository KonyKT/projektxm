<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="/">
  <html>
  <body >
  <h2 >My CD Collection</h2>  
  <xsl:apply-templates/>  
  </body>
  </html>
</xsl:template>

<xsl:template match="film">
  <p>
    <xsl:apply-templates select="tytul"/>  
    <xsl:apply-templates select="gatunki"/>
    <xsl:apply-templates select="dataPremiery"/>  
    <xsl:apply-templates select="rezyser"/>  
  </p>
</xsl:template>

<xsl:template match="tytul">
<FONT SIZE="4"><B>Tytul : </B></FONT>
<span style="color:#ff0000"><font size="6">
  <xsl:value-of select="."/></font></span>
  <br />
</xsl:template>

<xsl:template match="gatunki">
<FONT SIZE="3"><B>Gatunki: </B></FONT>
<span style="color:blue"><font size="4">
  <xsl:value-of select="."/></font></span>
  <br />
</xsl:template>

<xsl:template match="dataPremiery">
<FONT SIZE="3"><B>Data Premiery: </B></FONT>
<span style="color:blue"><font size="4">
  <xsl:value-of select="."/></font></span>
  <br />
</xsl:template>

<xsl:template match="rezyser">
<FONT SIZE="3"><B>Rezyer: </B></FONT>
<span style="color:blue"><font size="4">
  <xsl:value-of select="."/></font></span>
  <br />
</xsl:template>

</xsl:stylesheet>

