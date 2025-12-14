package com.example.ms_clientes.application.service.impl;

import org.springframework.stereotype.Service;

@Service
public class EmailTemplateService {

    public String templateContacto(String nombre, String email, String mensaje) {
        String safeMensaje = mensaje == null ? "" : mensaje.replace("\n", "<br/>");

        return """
            <div style="font-family: Arial, sans-serif; line-height:1.5;">
              <h2 style="color:#ea580c;">Nuevo mensaje de contacto - TechFactory</h2>
              <p><b>Nombre:</b> %s</p>
              <p><b>Email:</b> %s</p>
              <p><b>Mensaje:</b><br/>%s</p>
              <hr/>
              <p style="font-size:12px;color:#64748b;">
                Este correo fue enviado desde el formulario de contacto del sitio.
              </p>
            </div>
        """.formatted(nombre, email, safeMensaje);
    }
}