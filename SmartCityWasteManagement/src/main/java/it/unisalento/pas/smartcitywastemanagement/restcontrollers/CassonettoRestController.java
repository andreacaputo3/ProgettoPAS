package it.unisalento.pas.smartcitywastemanagement.restcontrollers;

import it.unisalento.pas.smartcitywastemanagement.domain.Cassonetto;
import it.unisalento.pas.smartcitywastemanagement.dto.CassonettoDTO;
import it.unisalento.pas.smartcitywastemanagement.repositories.CassonettoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cassonetti")
public class CassonettoRestController {

    @Autowired
    private CassonettoRepository cassonettoRepository;

    @GetMapping("/")
    public ResponseEntity<List<CassonettoDTO>> getAll() {
        List<Cassonetto> cassonetti = cassonettoRepository.findAll();
        List<CassonettoDTO> cassonettiDTO = cassonetti.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(cassonettiDTO, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CassonettoDTO> get(@PathVariable String id) {
        Optional<Cassonetto> optionalCassonetto = cassonettoRepository.findById(id);

        return optionalCassonetto.map(cassonetto -> new ResponseEntity<>(convertToDTO(cassonetto), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public CassonettoDTO createCassonetto(@RequestBody CassonettoDTO cassonettoDTO) {
        Cassonetto nuovoCassonetto = convertToEntity(cassonettoDTO);
        nuovoCassonetto = cassonettoRepository.save(nuovoCassonetto);
        return convertToDTO(nuovoCassonetto);
    }

    private CassonettoDTO convertToDTO(Cassonetto cassonetto) {
        CassonettoDTO cassonettoDTO = new CassonettoDTO();
        cassonettoDTO.setId(cassonetto.getId());
        cassonettoDTO.setTipoRifiuto(cassonetto.getTipoRifiuto());
        cassonettoDTO.setDataOraConferimento(cassonetto.getDataOraConferimento());
        cassonettoDTO.setCittadinoId(cassonetto.getCittadinoId());
        return cassonettoDTO;
    }

    private Cassonetto convertToEntity(CassonettoDTO cassonettoDTO) {
        Cassonetto cassonetto = new Cassonetto();
        cassonetto.setTipoRifiuto(cassonettoDTO.getTipoRifiuto());
        cassonetto.setDataOraConferimento(cassonettoDTO.getDataOraConferimento());
        cassonetto.setCittadinoId(cassonettoDTO.getCittadinoId());
        return cassonetto;
    }
}
