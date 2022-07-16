$(document).ready(function () {
    let input_fecha_min = $("#fecha-min");
    let input_fecha_max = $("#fecha-max");

    // Carga todos los productos al cargar la página
    //buscaPedidos();

    $(input_fecha_min).on("change", buscaPedidos);
    $(input_fecha_max).on("change", buscaPedidos);

    function buscaPedidos() {
        let fecha_min = $(input_fecha_min).val();
        let fecha_max = $(input_fecha_max).val();
        
        console.log(fecha_min)
        console.log(fecha_max)

        $.ajax({
            url: "/rest-pedidos/busqueda",
            data: {
                fecha_min,
                fecha_max
            },

            dataType: "json",

            success: function(pedidos) {
                console.log(pedidos)
                /*let caja_productos = $("#productos");

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
                }*/
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
