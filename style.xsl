<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" >
<xsl:output method="text" omit-xml-declaration="yes" indent="no"/>
<xsl:template match="/">
name,hasLift,about,type,Load_avg_5min,Load_avg_15min
<xsl:for-each select="//Description">
<xsl:value-of select="concat(name,',',hasLift,',',about,',',type,',',Load_avg_5min,',',Load_avg_15min,'&#xA;')"/>
</xsl:for-each>
</xsl:template>
</xsl:stylesheet>