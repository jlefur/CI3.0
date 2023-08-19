<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">

	<xsl:output method="html" indent="yes" doctype-public="-//W3C//DTD HTML 4.0//EN" />

	<xsl:template match="/">
		<html>
			<head>
				<title>Centre d'Informations (CI) - CBGP/IRD
				</title>
				<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
			</head>
			<body background="icons/CI_icons/CI_background.jpg">
				<table border="0" width="80">
					<TBODY>
						<TR>
							<TD width="80">
								<a href="index.htm" target="_top">
																		<IMG height="70" alt="HOME Centre d Informations (CI)" src="icons/CI_icons/CI_icon.gif"
										width="70" border="0"></IMG>
								</a>
							</TD>
						</TR>
						<TR>
							<TD width="80" align="center" valign="top">
								<p align="center">
									<font size="1">
										<a target="_top" href="index.htm">CI home</a>
									</font>
								</p>
							</TD>
						</TR>
					</TBODY>
				</table>
				<hr></hr>

				<!--Option d'affichage -->
				<xsl:choose>
					<!--Cas affichage de toutes les informations -->
					<xsl:when test="list/@type ='all'">
						<h2 >
							<xsl:value-of select="list/@number" />
							information items available in the Centre d'Informations (CI)
						</h2>
						<font size="3">
							<ul>
								<xsl:for-each select="list/information">
									<xsl:sort select="@uniqueTag" />
									<li>
										<xsl:value-of select="@uniqueTag" />
										&#160;
										<a href='information?idInformation={@idInformation}'>
											<xsl:value-of select="title" />
										</a>
										(
										<xsl:value-of select="subtitle" />
										)
									</li>
								</xsl:for-each>
							</ul>
						</font>
					</xsl:when>
					<!--Cas affichage des informations appartenant à un descripteur donné -->
					<xsl:otherwise>
						<h2 align="center">
							<xsl:value-of select="list/@number" />
							information items found about
							<xsl:value-of select="list/ @contents" />
						</h2>
						<font size="4">
							<ul>
								<xsl:apply-templates select='list/information' />
							</ul>
						</font>

					</xsl:otherwise>
				</xsl:choose>
			</body>
		</html>


	</xsl:template>
	<xsl:template match='information'>
		<li>
			<a href='information?idInformation={@idInformation}'>
				<xsl:value-of select="title" />
			</a>
			(
			<xsl:value-of select="subtitle" />
			)
		</li>
	</xsl:template>
</xsl:stylesheet>
