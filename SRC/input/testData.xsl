<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
		<html>
			<body>
				<h2>DATA LIST</h2>
				<table border="1">
					<tr bgcolor="#9acd32">
						<th>ID</th>
						<th>FIRST_NAME</th>
						<th>LAST_NAME</th>
					</tr>
					<xsl:for-each select="xml_main/person">
						<tr>
							<td>
								<xsl:value-of select="iden" />
							</td>
							<td>
								<xsl:value-of select="firstname" />
							</td>
							<td>
								<xsl:value-of select="lastname" />
							</td>
						</tr>
					</xsl:for-each>
				</table>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>