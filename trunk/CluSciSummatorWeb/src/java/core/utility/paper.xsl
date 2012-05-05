<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version= "1.0">

	<xsl:output method="html" indent="yes"/>

	<xsl:template match="/">
	  <html>
	  <body>
	   <xsl:apply-templates/>
	  </body>
	  </html>
	</xsl:template>

	<xsl:template match ="Paper">
		<xsl:template match="TITLE">
			<h1> <xsl:value-of select="TITLE"/></h1>
		</xsl:template>		
		
		<xsl:for-each select="AUTHORS/AUTHOR">
			<xsl:value-of select="AUTHORS/AUTHOR"/>
		</xsl:for-each>
	   
	</xsl:template>

</xsl:stylesheet>