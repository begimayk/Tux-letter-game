<?xml version="1.0" encoding="UTF-8"?>

<xs:stylesheet version="1.0"
                xmlns:xs="http://www.w3.org/1999/XSL/Transform" 
                xmlns:tux='http://myGame/tux'
>
    <xs:output method="html"/>

    <!-- La template principale sera:
            - header + body
            - ecrire le titre
            - modifier le style du tableau
            - ecrire le tableau contenant les mots du dictionnaire
            - appelle la template pour afficher les mots
    
         syntax recommendation http://www.w3.org/TR/xslt 
    -->
    <xs:template match="/">
        <html>
            <head>
                <title>dico.xsl</title>
            </head>
            <body>
                <h1>Dictionnaire</h1>
                <table>
                    <th>Mot</th>
                    <th>Niveau</th>
                </table>
                <xs:apply-templates select="//tux:mot">
                    <xs:sort select="."/>
                </xs:apply-templates> 
            </body>
        </html>
    </xs:template>

    <!--ICI on applique une template qui sert à afficher un tableau contenant:
            -le titre du tableau
            -chaque mot avec son niveau
            - appelle une autre template pour mettre dans l'ordre par niveau
    -->
    <xs:template match="tux:mot">
        <table>
            <tr>
                <td><xs:value-of select ="."/></td>
                <td><xs:value-of select ="@niveau"/></td>
            </tr>
        </table>
    </xs:template>


</xs:stylesheet>
