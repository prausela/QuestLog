import api from './api';

const imageServiceEndpoint   = 'images';

const getImage = async(imageName)  => {
    try {
        const endpoint = `${imageServiceEndpoint}/${imageName}`;
        const response = await api.get(endpoint);
        return response.data;
    } catch(err) {
        if(err.response) {
            return { status : err.response.status };
        } else {
            /* timeout */
        }
    }
}

const getImageLink = async(imageName)  => {
    try {
        const endpoint = `${process.env.PUBLIC_URL}/api/${imageServiceEndpoint}/${imageName}`;
        return endpoint;
    } catch(err) {
        if(err.response) {
            return { status : err.response.status };
        } else {
            /* timeout */
        }
    }
}

const ImageService = {
    getImage     : getImage,
    getImageLink : getImageLink,
}

export default ImageService;