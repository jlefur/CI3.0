<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
	<xsl:output method="html" indent="yes" doctype-public="-//W3C//DTD HTML 4.0//EN" />
	<xsl:template match="/">
		<!--balise correspondant la racine du document -->
		<html>
			<head>
				<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
			</head>
			<body background="icons/CI_icons/CI_background.jpg">
				<xsl:apply-templates />
				<!--boucle impliquant toutes les balises templates -->
				<p align="left">
					<table border="0" width="80">
						<TBODY>
							<TR>
								<TD width="80" valign="center" align="left">
									<a href="index.htm" target="_top">
										<IMG   width="70" alt="HOME Centre d Informations (CI)"
											src="icons/CI_icons/LogoCI-SanarSoft.gif" border="0"></IMG>
									</a>
								</TD>
							</TR>
							<TR>
								<TD width="80" align="left" valign="top">
									<p align="center">
										<font size="1">
											<a target="_top" href="index.htm">retour à l'accueil</a>
										</font>
									</p>
								</TD>
							</TR>
						</TBODY>
					</table>
				</p>
			</body>
		</html>
	</xsl:template>
	<xsl:template match="information">
		<!--trouve toutes balises "information" contenues dans la racine -->
		<head>
			<title>
				#[CI-SanarSoft] I
				<xsl:value-of select="@id" />
				:
				<xsl:value-of select="title" />
			</title>
		</head>
		<div align="left">
			<left>
				<table border="2" height="1" cellpadding="20" cellspacing="8">
					<!--cr�ation d'une table pour le titre -->
					<tr>
						<td valign="middle" align="left" background="icons/CI_icons/fond_cadre.1.jpg"
							height="1">
							<p align="center">
								<font face="Abadi MT Condensed Light" size="4">
									<b>
										<xsl:value-of select="title" />
									</b>
									<br />
									<!--recupere les donnees des balises "title","subtitle" les centre 
										dans la 2e colonne de la table -->
									<xsl:value-of select="subtitle" />
								</font>
							</p>
						</td>
					</tr>
				</table>
			</left>
		</div>
		<p></p><PRE><xsl:value-of select="shortDescription" /></PRE><p />
		<!--r�cup�re toutes les balises "support" contenues dans "information" -->
		<p />
		<p />
		<xsl:apply-templates select="Medium" />
		<hr />
		<font face="Arial Narrow">
			
			<u>Mots-clés</u> :
			<xsl:apply-templates select="Keyword" />
			<!--recherche la balise "Keyword" contenue dans la balise "information" -->
			<!--cr�ation d'un lien sur la donn�e de la balise "dimension" -->
			<p />
			<u>Dimension ATS</u>: 
			<a href="informationList?type=DimensionName&amp;contents={dimensionName}" target="_self">
				<xsl:value-of select="dimensionName" /></a>
				<font size="1"><br/><i>(NB: le lien ne fonctionne pas s'il n'y a que ce document dans la dimension)</i></font>
			<p />
			<!--<u>Catégories</u>:&#160;<xsl:apply-templates select="MetaKeyword" /> --><!--recherche la balise "MetaKeyword" contenue dans la balise "information" -->
			
			<u>Référence</u>:&#160;<xsl:value-of select="uniqueTag" /><br/>
			<u>Mise à jour</u>:&#160;<xsl:value-of select="entryDate" /><br/>
			<!-- , <u>type de document</u> :&#160;
			<xsl:apply-templates select="misc1Name" /> -->  <!--recherche la balise "niveau_de confiance" contenue dans la balise "information" -->
			<!--r�cup�re la donn�e de l'attribut id de l'information -->
			<u>Auteurs</u>:<xsl:apply-templates select="Misc2" /><br/>
			<u>Rédaction</u>:&#160;<xsl:apply-templates select="authorfullName" /><br/>
			<br/><u>Suggestion:</u>&#160;
			<xsl:apply-templates select="suggestion" />
			<!-- <p><a href="archives/{@id}.pdf " target="_new"> <img border="0" src="icons/bprint.gif" 
				align="baseline"></img> pdf printable version</a></p> -->
			
		</font>
	</xsl:template>
	<xsl:template match="entryDate">
		<!--<a href="informationList?type=date&amp;contents={@elem}"> --> <!--selection et cr�ation d'un lien sur la donn�e de la balise "date" -->
		<xsl:value-of select="." />
		<!--</a> -->
	</xsl:template>
	<xsl:template match="dimensionName">
		<a href="informationList?type=dimensionName&amp;contents={@elem}">
			<!--selection et cr�ation d'un lien sur la donn�e de la balise "dimensionName" -->
			<xsl:value-of select="." />
		</a>
	</xsl:template>
	<xsl:template match="authorfullName">
		<a href="informationList?type=fullName&amp;contents={@elem}">
			<!--selection et cr�ation d'un lien sur la donn�e de la balise "niveau_de 
				confiance" -->
			<u>
				<xsl:value-of select="." />
			</u>
		</a>
	</xsl:template>
	<xsl:template match="misc1Name">
		<!--<a href="informationList?type=misc1Name&amp;contents={@elem}"> -->
		<!--selection et cr�ation d'un lien sur la donn�e de la balise "niveau_de 
			confiance" -->
		<u>
			<xsl:value-of select="." />
		</u>
		<!--</a> -->
	</xsl:template>
	<xsl:template match="sourceheader">
		<a href="informationList?type=sourceheader&amp;contents={@elem}">
			<!--selection et cr�ation d'un lien sur la donn�e de la balise "source" -->
			<xsl:value-of select="." />
		</a>
	</xsl:template>
	<xsl:template match="MetaKeyword">
		<xsl:for-each select="MetaKeyword">
			<a href="metaDescriptorInstances?type=MetaKeyword&amp;contents={@elem}">
				<!--selection et cr�ation d'un lien sur la donn�e dechaque balise balise 
					"composant" -->
				<xsl:value-of select="." />
			</a>
			,&#160;
		</xsl:for-each>
	</xsl:template>
	<xsl:template match="Misc2">
		<xsl:for-each select="Misc2">
				<xsl:value-of select="." />,&#160; 
		</xsl:for-each>
	</xsl:template>
	
	<xsl:template match="Keyword">
		<xsl:for-each select="Keyword">
			<xsl:if test="@count &gt; 1">
			    <a href="informationList?type=keywordName&amp;contents={@elem}">
					<xsl:value-of select="." />
				</a>
			</xsl:if>
			<xsl:if test="@count = 1">
				<xsl:value-of select="." />
			</xsl:if>
			/&#160;<!-- , -->

		</xsl:for-each>
	</xsl:template>
	<xsl:template match="Medium">
		<td valign="middle" align="center" height="1">
			<!--placement des donn�es de la 1�re colonne de la table -->
			<xsl:for-each select="Medium">
				<!--r�cup�re et cr�e des liens sur les fichiers et logos de chaque balise 
					"support" contenues dans "information" -->

				<!-- ap.2015 if local : append 'infos/' -->
				<xsl:if test="@fileType = 'local'">
					<a href="archives/{@fileName} " target="_self">
					<img border="0" src="icons/mediaTypes/{@iconFileName}" width="100"
						align="texttop"
						alt="(note: click on the icon opens within the frame, click on the text opens in full window)" />
					</a>
					<a href="archives/{@fileName} " target="_blank">
						<xsl:value-of select="." />
					</a>
					&#160;
				</xsl:if>

				<!-- ap.2015 if url : leave fileName -->
				<xsl:if test="@fileType = 'url'">
					<a href="{@fileName} " target="_self">
					<img border="0" src="icons/mediaTypes/{@iconFileName}" width="100"
						align="texttop"
						alt="(note: click on the icon opens within the frame, click on the text opens in full window)" />
					</a>
					<a href="{@fileName} " target="_blank">
						<xsl:value-of select="." />
					</a>
					&#160;
				</xsl:if>

				
			</xsl:for-each>
		</td>
	</xsl:template>
	<!--Suggestions � proposer -->
	<xsl:template match="suggestion">
		<xsl:for-each select="suggestion">
			<a href="information?idInformation={@id}"> <!--selection et cr�ation d'un lien sur la donn�e de chaque balise "suggestion" -->
				<xsl:value-of select="." />
				(
				<xsl:value-of select="@weight" />
				)
			</a>
		</xsl:for-each>
	</xsl:template>
</xsl:stylesheet>
