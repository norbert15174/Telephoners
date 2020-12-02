package pl.telephoners.mappers;

import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;
import pl.telephoners.DTO.PostDTO;
import pl.telephoners.models.Post;

import java.util.ArrayList;
import java.util.List;

@Component
@NoArgsConstructor
public class PostObjectMapperClass {

    //Init model mapper PersonalData to PersonalDataDTO
    private ModelMapper postObjectMapper(){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<Post, PostDTO>() {
            @Override
            protected void configure() {
                map().setId(source.getId());
                map().setPostName(source.getPostName());
                map().setTopic(source.getTopic());

            }
        });
        return modelMapper;
    }

    //Return mapped models
    public List<PostDTO> mapPostsToPostsDTO(List<Post> Posts){
        List<PostDTO> PostsDTO = new ArrayList<>();
        Posts.forEach((pd -> PostsDTO.add(postObjectMapper().map(pd,PostDTO.class))));
        return PostsDTO;
    }

    //Return mapped model
    public PostDTO mapPostToPostDTO(Post post){
        PostDTO postDTO;
        postDTO = postObjectMapper().map(post,PostDTO.class);
        return postDTO;
    }





}
