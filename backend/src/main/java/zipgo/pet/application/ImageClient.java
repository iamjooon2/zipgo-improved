package zipgo.pet.application;

import org.springframework.web.multipart.MultipartFile;

public interface ImageClient {

    String upload(String name, MultipartFile file);

}