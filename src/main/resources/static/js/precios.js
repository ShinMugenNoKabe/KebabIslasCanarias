$(document).ready(function () {
    let input_precio = $("#precio");
    let input_porcentaje_descuento = $("#porcentaje-descuento");
    let input_precio_descuentado = $("#precio-descuentado");

    $(input_precio).on("input", calculaPrecioDescuentado);
    $(input_porcentaje_descuento).on("input", calculaPrecioDescuentado);

    function calculaPrecioDescuentado() {
        let precio = $(input_precio).val();
        let porcentaje_descuento = $(input_porcentaje_descuento).val();

        if (precio !== null && porcentaje_descuento !== null) {
            if (porcentaje_descuento === 0) {
                $(input_precio_descuentado).val(precio);
            } else {
                let precio_calculado =
                        (precio - (precio * porcentaje_descuento / 100));

                $(input_precio_descuentado).val(precio_calculado);
            }
        }
    }
});