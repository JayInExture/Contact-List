package com.example.fullstack_ContactList.service;

import com.example.fullstack_ContactList.domain.Contacts;
import com.example.fullstack_ContactList.repository.ContactRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.example.fullstack_ContactList.constant.Constant.PHOTO_DIRECTORY;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Service
@Log4j2
@Transactional(rollbackOn =  Exception.class)
@RequiredArgsConstructor
public class ContactService {
    private  final ContactRepository contactRepository;

    public Page<Contacts> getAllContact(int page , int size){
        return contactRepository.findAll(PageRequest.of(page,size, Sort.by("name")));
    }

    public Contacts getContact(Long Id){
        return contactRepository.findById(Id).orElse(null);
    }


    public Contacts createContact(Contacts contacts){
        return contactRepository.save(contacts);
    }


    public String uploadPhoto(Long Id, MultipartFile file){
        log.info("Saving Image...-> "+ file+"..With Id.."+Id);
        Contacts contacts = getContact(Id);
        String photoUrl = photoFunction.apply(Id ,file);
        contacts.setDpImageUrl(photoUrl);
        contactRepository.save(contacts);
        return photoUrl;
    }



    private final Function<String, String> fileExtension = filename -> Optional.of(filename).filter(name -> name.contains(".")).map(name ->"."+name.substring(filename.lastIndexOf(".")+1)).orElse(".png");

    private final BiFunction<Long ,MultipartFile, String> photoFunction = (id, image) -> {
        String filename = id + fileExtension.apply(image.getOriginalFilename());
        try {
            Path fileStorageLocation = Paths.get(PHOTO_DIRECTORY).toAbsolutePath().normalize();
            if(!Files.exists(fileStorageLocation)){
                Files.createDirectories(fileStorageLocation);
            }
            Files.copy(image.getInputStream(), fileStorageLocation.resolve(filename),REPLACE_EXISTING);
            return ServletUriComponentsBuilder.fromCurrentContextPath().path("/contacts/image/"+filename).toUriString();
        }
        catch (Exception exception){
            throw  new RuntimeException("Unable to save image");
        }
    };

}
