package zipgo.review.fixture;

import zipgo.member.domain.Member;
import zipgo.petfood.domain.PetFood;
import zipgo.review.domain.Review;
import zipgo.review.dto.request.CreateReviewRequest;
import zipgo.review.dto.request.UpdateReviewRequest;

import java.util.ArrayList;
import java.util.List;

import static zipgo.review.domain.type.StoolCondition.DIARRHEA;
import static zipgo.review.domain.type.StoolCondition.SOFT_MOIST;
import static zipgo.review.domain.type.TastePreference.EATS_VERY_WELL;
import static zipgo.review.domain.type.TastePreference.NOT_AT_ALL;

public class ReviewFixture {

    public static CreateReviewRequest 리뷰_생성_요청(Long 식품_id) {
        return new CreateReviewRequest(
                식품_id,
                5,
                "우리 아이랑 너무 잘 맞아요!",
                "정말 잘 먹어요",
                "촉촉 말랑해요",
                new ArrayList<>()
        );
    }

    public static UpdateReviewRequest 리뷰_수정_요청() {
        return new UpdateReviewRequest(
                4,
                "change comment",
                "잘 먹는 편이에요",
                "잘 모르겠어요",
                List.of("먹고 토해요")
        );
    }

    public static Review 극찬_리뷰_생성(Member 멤버, PetFood 식품) {
        return Review.builder()
                .member(멤버)
                .petFood(식품)
                .rating(5)
                .comment("우리 아이랑 너무 잘 맞아요!")
                .tastePreference(EATS_VERY_WELL)
                .stoolCondition(SOFT_MOIST)
                .build();
    }

    public static Review 혹평_리뷰_생성(Member 멤버, PetFood 식품, List<String> 이상반응들) {
        Review 리뷰 = Review.builder()
                .member(멤버)
                .petFood(식품)
                .rating(1)
                .comment("우리 아이가 한 입 먹고 더 안 먹어요 ㅡ.ㅡ 책임지세요.")
                .tastePreference(NOT_AT_ALL)
                .stoolCondition(DIARRHEA)
                .build();
        리뷰.addAdverseReactions(이상반응들);
        return 리뷰;
    }

}