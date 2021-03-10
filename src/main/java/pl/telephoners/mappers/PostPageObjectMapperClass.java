package pl.telephoners.mappers;

import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;
import pl.telephoners.DTO.PostDTO;
import pl.telephoners.DTO.PostPageDTO;
import pl.telephoners.models.Post;

import java.util.ArrayList;
import java.util.List;

@Component
@NoArgsConstructor
public class PostPageObjectMapperClass {


    private ModelMapper postObjectMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<Post, PostPageDTO>() {
            @Override
            protected void configure() {
                map().setId(source.getId());
                map().setPostName(source.getPostName());
                map().setTopic(source.getTopic());
                map().setAuthorName(source.getAuthor().getFirstName());
                map().setAuthorSurname(source.getAuthor().getLastName());
                map().setContent(source.getContent());
                map().setMainPhoto(source.getMainPhoto());
                map().setGalleries(source.getGalleries());
                map().setPostDate(source.getPostDate());

            }
        });
        return modelMapper;
    }

    //Return mapped models
    public List<PostPageDTO> mapPostsToPostsDTO(List<Post> posts) {
        List<PostPageDTO> postPageDTOS = new ArrayList<>();
        posts.forEach((pd -> postPageDTOS.add(postObjectMapper().map(pd, PostPageDTO.class))));
        return postPageDTOS;
    }

    //Return mapped model
    public PostPageDTO mapPostToPostDTO(Post post) {
        PostPageDTO postPageDTO;
        postPageDTO = postObjectMapper().map(post, PostPageDTO.class);
        return postPageDTO;
    }


}
