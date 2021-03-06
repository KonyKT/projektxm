<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:template match="/">
    <html>
      <body >
        <h2 style="display: inline-block;float:left">
          <xsl:apply-templates/>
        </h2>
        <h3 style="display: inline-block; float:left">
          <table border="1">
            <tr bgcolor="#c45345">
              <th style="text-align:left">Gatunek</th>
              <th style="text-align:left">Skrot</th>
            </tr>
            <xsl:for-each select="listaFilmow/gatunki/typ">
              <tr>
                <td><xsl:value-of select="."/></td>
                <td><xsl:value-of select="@skrot"/></td>
              </tr>
            </xsl:for-each>
          </table>
        </h3>
      </body>
    </html>
  </xsl:template>

  <xsl:template match="film">
    <p>
      <xsl:apply-templates select="tytul"/>
      <xsl:apply-templates select="gatunek"/>
      <xsl:apply-templates select="dataPremiery"/>
      <xsl:apply-templates select="rezyser"/>
    </p>
  </xsl:template>



  <xsl:template match="tytul">
    <FONT SIZE="4"><B>Tytul : </B></FONT>
    <span style="color:#ff0000"><font size="6">
      <xsl:value-of select="."/></font></span>
    <font size="1">
      (Rok:     <xsl:value-of select="@rok"/>)
    </font>
    <br />
  </xsl:template>

  <xsl:template match="gatunek">
    <FONT SIZE="3"><B>Gatunki: </B></FONT>
    <span style="color:blue"><font size="4">
      <xsl:value-of select="@skrot" /></font></span>
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

