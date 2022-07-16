$(document).ready(function () {
    let check_recogida_en_tienda = $("#recoger-en-tienda");
    let input_direccion_de_envio = $("#direccion-envio");

    $(check_recogida_en_tienda).change(function () {
        if (this.checked) {
            $(input_direccion_de_envio).attr("disabled", "disabled");
        } else {
            $(input_direccion_de_envio).attr("disabled", null);
        }
    });
});
