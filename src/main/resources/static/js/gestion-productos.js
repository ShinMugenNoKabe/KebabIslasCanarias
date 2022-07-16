$(document).ready(function () {
    let barra_busqueda_nombre = $("#busqueda-nombre");
    let categorias = $("#categorias");
    let ordenes_de_precio = $("#ordenes-de-precio");
    let precio_minimo = $("#precio-minimo");
    let precio_maximo = $("#precio-maximo");
    let productos_en_oferta = $("#productos-en-oferta");

    // Carga todos los productos al cargar la página
    buscaProductos();

    $(barra_busqueda_nombre).on("input", buscaProductos);
    $(categorias).on("change", buscaProductos);
    $(ordenes_de_precio).on("change", buscaProductos);
    $(precio_minimo).on("input", buscaProductos);
    $(precio_maximo).on("input", buscaProductos);
    $(productos_en_oferta).on("change", buscaProductos);

    function buscaProductos() {
        let nombre = $(barra_busqueda_nombre).val();
        let id_categoria = $(categorias).val();
        let orden = $(ordenes_de_precio).val();
        let precio_min = $(precio_minimo).val();
        let precio_max = $(precio_maximo).val();
        let en_oferta = $(productos_en_oferta).is(":checked");

        $.ajax({
            url: "/rest-productos/busqueda",
            data: {
                nombre,
                id_categoria,
                orden,
                precio_min,
                precio_max,
                en_oferta,
                administrador: "ESADMIN"
            },

            dataType: "json",

            success: function (productos) {
                let tabla_productos = $("#tabla-productos");

                if (productos.length === 0) {
                    tabla_productos.html("<tr><td colspan='8'>No se ha encontrado ningún producto.</td></tr>");
                } else {
                    let template = "";

                    productos.forEach(producto => {
                        template += `
                                    <tr class="table-warning">
                                        <td>${producto.codprod}</td>
                                        <td><img src="/imagenes/${producto.imagen}"
                                            width="170px" height="100px"
                                            alt="${producto.nombre}"></td>
                                        <td>${producto.nombre}</td>
                                        <td>${producto.precio}€</td>
                                        <td>${producto.porcentajeDeOferta}%</td>
                                        <td>${producto.precioConDescuento}€</td>
                                        <td>${producto.categoria.nombre}</td>
                                        <td>` + tickCruz(producto.disponible) + `</td>
                                        <td><a href="/producto/editar/${producto.codprod}"
                                            class="btn btn-warning">M</a></td>
                                    </tr>
                                `
                    });

                    tabla_productos.html(template);

                    // Actualiza los listeners
                    //let boton_anadir_pedido = $(".boton-anadir-pedido");
                    //$(boton_anadir_pedido).click(anadirProductoAPedido);
                }
            }
        });
    }
    
    function tickCruz(disponible) {
        if (disponible === true) {
            return "&#9989";
        } else {
            return "&#10060";
        }
    }
});
