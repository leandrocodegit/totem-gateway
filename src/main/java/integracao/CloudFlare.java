package integracao;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

//@FeignClient(value = "cloudflare-url-turnstyle", url = "${cloudflare.url}")
public interface CloudFlare {

   // @PostMapping
    //public void criarUsuario(@RequestBody  TurnStyleValidate turnStyleValidate);
}
