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
                en_oferta
            },

            dataType: "json",

            success: function(productos) {
                let caja_productos = $("#productos");

                if (productos.length === 0) {
                    caja_productos.html("<h1>No se ha encontrado ningún producto.</h1>");
                } else {
                    let template = "";

                    productos.forEach(producto => {
                        template += `
                                    <div class="col-sm-6 col-md-6 col-lg-4 col-xl-3 mb-2">
                                        <div class="card text-center" style="width: 18rem;">
                                            <img src="/imagenes/${producto.imagen}"
                                                 class="card-img-top"
                                                 alt="${producto.nombre}"
                                                 width="200px" height="200px">

                                            <div class="card-body">
                                                <h4 class="card-title">${producto.nombre}</h4>
                                            </div>

                                            <ul class="list-group list-group-flush">
                                                <li class="list-group-item">${producto.categoria.nombre}</li>
                                                <li class="list-group-item">` + getTextoDePrecio(producto) + `</li>
                                            </ul>

                                            <div class="card-body" data-id="${producto.codprod}">
                                                <input type="number" class="form-control mb-1" min="0" value="1">
                                                <button class="btn btn-danger boton-anadir-pedido">Añadir al pedido</button>
                                            </div>
                                        </div>
                                    </div>
                                `
                    });

                    caja_productos.html(template);
                    
                    // Actualiza los listeners
                    let boton_anadir_pedido = $(".boton-anadir-pedido");
                    $(boton_anadir_pedido).click(anadirProductoAPedido);
                }
            }
        });
    }

    function getTextoDePrecio(producto) {
        if (producto.precio === producto.precioConDescuento) {
            return (producto.precio + "€");
        }

        let texto = `<del>${producto.precio}€</del> <b>${producto.precioConDescuento}€</b>`;

        return texto;
    }
    
    function anadirProductoAPedido() {
        let producto_pulsado = $(this).parent();
        
        let id_producto = $(producto_pulsado).attr("data-id");
        let cantidad = $(this).siblings("input").val();
        
        let caja_success = $("#caja-success");

        $.ajax({
            url: "/rest-pedidos/anadir-producto-a-pedido",
            type: "get",
            data: {
                id_producto,
                cantidad
            },

            dataType: "json",

            success: function(respuesta) {
                console.log(respuesta);
                    caja_success.html(respuesta.msg);
                    caja_success.removeClass("d-none");
            },
            
            error: function(respuesta) {
                console.log("ERROR: " + respuesta);

                    caja_success.removeClass("d-none");
            }
        });
    }
});
