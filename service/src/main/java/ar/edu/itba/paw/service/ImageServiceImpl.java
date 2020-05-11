package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.ImageDao;
import ar.edu.itba.paw.interfaces.service.ImageService;
import ar.edu.itba.paw.model.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Optional;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageDao imageDao;

    @Override
    public Optional<Image> findByImageName(String image_name) {
        return imageDao.findByImageName(image_name);
    }

    @Override
    public Image uploadImage(String image_name, byte[] image_data) {
        return imageDao.uploadImage(image_name, image_data);
    }
}