# Meli Challenge
Buscador desarrollado para el programa Mercadolibre Mobile Candidate

Se decidió utilizar las siguientes dependencias

implementation 'com.android.support.constraint:constraint-layout:1.1.3': Se utilizaron "constraint layout" debido a su facilidad para hacer activity´s que se comporten bien ante varias pantallas y resoluciones. También a su poder de poder poner los componentes relativos a otros componentes y de ésta forma lograr diseños responsive.

implementation 'com.android.support:recyclerview-v7:27.1.0': Las RecyclerView hacen uso del patrón ViewHolder. El RecyclerView es muy potente, y flexible. Hace las cosas un poco más complejas en RecyclerView pero muchos de los problemas que enfrentamos en el ListView se resuelven de manera eficiente.

implementation 'com.android.support:cardview-v7:27.1.0': Se decidió utilizar CardView para mostrar los elemenos resultantes de la busqueda de una manera mas ordenada, atractiva y eficiente.

implementation 'com.github.JakeWharton:ViewPagerIndicator:2.4.1': Librería de tercero utilizada para mostrar el indicador de pagina actual al visualizar las imagenes de un producto.

implementation 'com.github.bumptech.glide:glide:4.7.1': Librería de un tercero utilizada para mostrar mas facilmente la galería de imagenes de un produto en forma de carusel.

Nota: Para poder acceder a los recursos, pictures" y "description", de los "items" necesite que el usuario del sistema se loguee en la API de MercadoLibre, para ésto simplemente use el ejemplo adjunto el la SDK de Android. Como simplificación al ser una app de muestra decidí que al iniciar la primera vez la app nos pida loguearnos, luego éste usuario será el que utilize la app. 
Debido principalmente a cuestiones de tiempo no se desarrollo la funcionalidad para permitir el logueo y deslogueo de usuarios.
