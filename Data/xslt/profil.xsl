<?xml version="1.0" encoding="UTF-8"?>

<!--
    Document   : profil.xsl
    Created on : 9 novembre 2021, 17:48
    Author     : cestm
    Description:
        Purpose of transformation follows.
-->

<xsl:stylesheet version="1.0"                
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
                xmlns='http://myGame/tux'
>
    <xsl:output method="html"/>


    <!-- La template principale sera:
            - header + body
            - ecrire le titre
            - modifier le style du tableau
            - ecrire le nom du joueur + anniversaire
            - affiche sa photo
            - appelle la template pour afficher les parties
    
         syntax recommendation http://www.w3.org/TR/xslt 
    -->
    <xsl:template match="/">
        <html>
            <head>
                <title>profil.xsl</title>
                <style> table {
                table-layout: fixed;
                width: 90%;
                border-collapse: collapse;
                border: 3px solid rgb(35, 94, 102);
                margin-left: auto;
                margin-right: auto;
                background : white;
                }

                td {
                  border: 1px solid rgb(50, 84, 90);
               }

                th, td {
                  padding: 20px;
                  text-align: center;
                
                }
                h1 {
                  font-size: 36px;
                  line-height: 40px;
                  text-align: center;
               }

               h2 {
                  font-size: 30px;
                  line-height: 40px;
                  text-align: center;
               }

               h3{
                  font-size: 24px;
                  line-height: 40px;
                  text-align: center;
               }
               h4{
                  font-size: 16px;
                  line-height: 20px;
                  text-align: center;
               }
               body{
                  background: rgb(135, 200, 211);
               }
               div{
                  background : white;
               }</style>
            </head>
            <body>
                <h4>Profil du Joueur</h4>
                <h4>Informations </h4>
                <div><h3>
                    Nom joueur :
                    <xsl:value-of select="//nom" />
                </h3>         
                <h3>
                    Anniversaire :
                    <xsl:value-of select="//anniversaire" />
                </h3></div>
                
                <h2>Parties jouées</h2>
                
                <table>
                    <thead>
                        <th>Date</th>
                        <th>Mot</th>
                        <th>Niveau</th>
                        <th>Trouvé</th>
                        <th>Temps</th>
                    </thead>
                    <tbody>
                        <xsl:apply-templates select="//parties"/>
                    </tbody>
                </table>
            </body>
        </html>
    </xsl:template>
   
    
    <!--cette template sert à afficher 
        les informations des différentes parties
    -->
  
    <xsl:template match="partie">
    <xsl:choose>
        <xsl:when test="@trouvé">
            <tr>
                <td><xsl:value-of select ="@date"/></td>
                <td><xsl:value-of select="mot"/></td>
                <td><xsl:value-of select="mot/@niveau"/></td>
                <td><xsl:value-of select="@trouvé"/> %</td>
                <td>Temps écoulé</td>
            </tr>
        </xsl:when>
        <xsl:otherwise>
            <tr>
                <td><xsl:value-of select ="@date"/></td>
                <td><xsl:value-of select="mot"/></td>
                <td><xsl:value-of select="mot/@niveau"/></td>
                <td> 100 %</td>
                <td><xsl:value-of select="temps"/></td>
            </tr>
        </xsl:otherwise>
    </xsl:choose>
    </xsl:template>

</xsl:stylesheet>
