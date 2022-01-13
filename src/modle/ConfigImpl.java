package modle;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import utility.Config;

@Builder (toBuilder = true)
public class ConfigImpl implements Config {

    @Getter @Setter
    @Builder.Default
    private String root = "./KEY";
    @Getter @Setter
    @Builder.Default
    private String masterFolder = "/master";
    @Getter @Setter
    @Builder.Default
    private String masterFileName = "/master.pw";
    @Getter @Setter
    @Builder.Default
    private String passwordFolder = "/password";
    @Getter @Setter
    @Builder.Default
    private String passwordFileName = "/password.pw";

}
