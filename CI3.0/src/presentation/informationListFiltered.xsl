<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">

	<xsl:output method="html" indent="yes" doctype-public="-//W3C//DTD HTML 4.0//EN" />

	<xsl:template match="/">
		<html>
			<head>
				<title>CNSHB - IRD: Centre d'Information (CI) sur les pêches en
					Guinée </title>
				<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
			</head>
			<body background="icons/FIB14.gif" bgcolor="#E1E1FF">
				<!--Option d'affichage -->
				<xsl:choose>
					<!--Cas afichage de toutes les informations -->
					<xsl:when test="liste/@type ='tous'">
						<h2 align="center">
							<xsl:value-of select="liste/@nombre" />
							Informations présentes en tout dans le Centre d'informations
						</h2>
						<font size="3">
							<ul>
								<xsl:for-each select="liste/information">
									<xsl:sort select="@id" />
									<li>
										<xsl:value-of select="@id" />
										&#160;
										<a href='GetInfo.html?id={@id}'>
											<xsl:value-of select="titre" />
										</a>
										(
										<xsl:value-of select="sujet" />
										)
									</li>
								</xsl:for-each>
							</ul>
						</font>
					</xsl:when>
					<!--Cas afichage des informations appartenant à un descripteur donné -->
					<xsl:otherwise>
						<h2 align="center">
							<xsl:value-of select="liste/@nombre" />
							Informations trouvées sur
							<xsl:value-of select="liste/@type" />
							&#160;
							<xsl:value-of select="liste/ @contenu" />
						</h2>
						<font size="4">
							<ul>
								<xsl:apply-templates select='liste/information' />
							</ul>
						</font>

						<!--Lignes destinées au filtrage -->
						<div align="right">
							<table border="0" cellspacing="1">
								<tr>
									<td width="49" valign="top">
										<a
											href='Filtre.html?type={liste/@type}&amp;contents={liste/@contenu}'>
											<img border="0" src="icons/bfiltre_yes.gif" alt="filtrer sur ce concept">
											</img>
										</a>
									</td>
									<td width="50" valign="top">
										<a href='Filtre.html?type=AnnulerFiltre&amp;contents="" '>
											<img border="0" src="icons/bfiltre_no.gif" alt="supprimer le filtre en cours">
											</img>
										</a>
									</td>
									<td>
										filtre en cours:
										<br></br>
										<font color="#FF0000">
											<xsl:value-of select="liste/@filtreType" />
											&#160;
											<xsl:value-of select="liste/@filtreContenu" />
										</font>
									</td>
								</tr>
							</table>
						</div>
						<!--Fin des lignes destinées au filtrage -->
					</xsl:otherwise>
				</xsl:choose>
			</body>
		</html>


	</xsl:template>
	<xsl:template match='information'>
		<li>
			<a href='GetInfo.html?id={@id}'>
				<xsl:value-of select="titre" />
			</a>
			(
			<xsl:value-of select="sujet" />
			)
		</li>
	</xsl:template>
</xsl:stylesheet>
