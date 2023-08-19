<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
	<xsl:output method="html" indent="yes" doctype-public="-//W3C//DTD HTML 4.0//EN" />
	<xsl:template match="list">
		<html>
			<head>
				<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
			</head>
			<body background="icons/FIB14.gif">
				<h2 align="center">
					<xsl:value-of select="@number" />
					item(s) for
					<xsl:value-of select="@type" />
				</h2>
				<ul>
					<xsl:for-each select="instance">
						<li>
							<a
								href="informationList?type={champ1/@type}&amp;contents={champ1/@contents}"
								target="principal">
								<xsl:value-of select="champ1" />
							</a>
							&#160;
							<xsl:if test="champ1/@type='source'">
								<xsl:value-of select="champ2" />
							</xsl:if>
						</li>
					</xsl:for-each>
				</ul>
				<hr></hr>
				<table border="0" width="100%">

					<TBODY>
						<TR>
							<TD width="60">
								<a href="CI.htm" target="_top">
									<IMG height="45" alt="SimMasto Centre d Informations (CI)"
										src="icons/blogo_CI.gif" width="53" border="1"></IMG>
								</a>
							</TD>
							<TD width="60" align="center">
								<a target="_top" href="index.htm">
									<IMG height="50" alt="SimMasto home" src="icons/bhome.jpg"
										width="45" border="1"></IMG>
								</a>
							</TD>
							<TD width="60">
								<a target="_top" href="Model/index_model.htm">
									<IMG height="45" alt="SimMasto Model part" src="icons/blogo_MDL.gif"
										width="53" border="1"></IMG>
								</a>
							</TD>
							<TD />
							<TD width="70" align="center">
								<a href="CI_statique/NoteOnSoftware.htm">
									<IMG height="48" alt="note on the software" src="icons/bmanuel.gif"
										width="48" border="0"></IMG>
								</a>
							</TD>
							<TD width="50" align="center">
								<a href="mailto:lefur@ird.fr?subject=CI-SimMasto">
									<IMG height="48" alt="email contact" src="icons/bmail2.gif"
										width="48" border="0"></IMG>
								</a>
							</TD>
						</TR>
						<TR>
							<TD width="60" align="center" valign="top">
								<p align="center">
									<font size="1">
										<a target="_top" href="CI.htm">Centre d'Informations</a>
									</font>
								</p>
							</TD>
							<TD width="60" align="center" valign="top">
								<p align="center">
									<font size="1">
										<a target="_top" href="index.htm">
											SimMasto
											<br>Home</br>
										</a>
									</font>
								</p>
							</TD>
							<TD width="60" align="center" valign="top">
								<p align="center">
									<font size="1">
										<a target="_top" href="Model/index_model.htm">
											Model
											<br />
											part
											<br />
										</a>
									</font>
								</p>
							</TD>
							<TD />
							<TD width="70" align="center" valign="top">
								<p align="center">
									<font size="1">
										<a href="CI_statique/NoteOnSoftware.htm">Note on the software</a>
									</font>
								</p>
							</TD>
							<TD width="50" align="center" valign="top">
								<p align="center">
									<font size="1">
										<a href="mailto:lefur@ird.fr?subject=CI-SimMasto">Email contact</a>
									</font>
								</p>
							</TD>
						</TR>
					</TBODY>
				</table>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>
