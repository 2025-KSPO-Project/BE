package com.kspo.carefit.damain.carpool.service;

import com.kspo.carefit.base.client.GoogleMapClient;
import com.kspo.carefit.base.config.exception.BaseExceptionEnum;
import com.kspo.carefit.base.config.exception.domain.BaseException;
import com.kspo.carefit.base.helper.MathHelper;
import com.kspo.carefit.damain.carpool.dto.NearBySpot;
import com.kspo.carefit.damain.carpool.dto.request.CarpoolPostRequest;
import com.kspo.carefit.damain.carpool.dto.request.GetGradiantRequest;
import com.kspo.carefit.damain.carpool.dto.response.*;
import com.kspo.carefit.damain.carpool.entity.Carpool;
import com.kspo.carefit.damain.carpool.repository.CarpoolRepository;
import com.kspo.carefit.damain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CarpoolService {

    private final CarpoolRepository carpoolRepository;
    private final GoogleMapClient googleMapClient;
    private final MathHelper mathHelper;

    /*
    기본적인 CRUD 메소드 모음
     */

    // 작성자의 id로 카풀 객체를 가져오는 메소드
    public List<Carpool> findByWriterId(Long writerId){

        List<Carpool> carpools = carpoolRepository.findByWriterId(writerId);

        if (carpools.isEmpty()) {
            throw new BaseException(BaseExceptionEnum.CARPOOL_NOT_FOUND);
        }

        return carpools;
    }

    // id로 카풀 객체 받아오기
    public Carpool findById(Long id){
        return carpoolRepository
                .findByIdWithWriter(id)
                .orElseThrow(()->
                        new BaseException(BaseExceptionEnum.CARPOOL_NOT_FOUND));
    }

    // 작성자와 사용자의 일치여부 확인하기
    public boolean checkPoster(Long postId,
                               Long userId){

        Long posterId = findById(postId).getWriter().getId();

        return posterId.equals(userId);
    }

    // 카풀 객체 삭제하기
    public void deleteCarpool(Carpool carpool){
        carpoolRepository.delete(carpool);
    }

    /*
    카풀 업로드 관련
     */

    // 카풀 포스팅 업로드 하기
    public Carpool createCarpoolPost(CarpoolPostRequest carpoolPostRequest,
                                                 User writer){

        Carpool carpool = Carpool.builder()
                .title(carpoolPostRequest.title()) // 제목
                .content(carpoolPostRequest.content()) // 내용
                .postedAt(Instant.now()) // 게시 날짜
                .meetAt(carpoolPostRequest.meetAt()) // 만남 날짜
                .maxCount(carpoolPostRequest.maxCount()) // 최대 인원
                .writer(writer) // 작성자
                .start(carpoolPostRequest.toStart()) // 시작지점
                .destination(carpoolPostRequest.toDestination()) // 최종지점
                .spot(carpoolPostRequest.toSpot()) // 중간 지점
                .build();

        carpoolRepository.save(carpool);

        return carpool;

    }

    // 카풀 포스팅 삭제하기
    public CarpoolDeleteResponse deleteCarpoolByWriterId(Carpool carpool,Long writerId){

        if((carpool.getWriter().getId().equals(writerId))){
            deleteCarpool(carpool);
            return new CarpoolDeleteResponse(
                    carpool.getId(),
                    carpool.getWriter().getNickname(),
                    "해당 포스팅이 삭제되었습니다.");
        }

        throw new BaseException(BaseExceptionEnum.WRITER_UNMATCHED);

    }

    // 단일 카풀 포스팅 가져오기
    public CarpoolResponse getCarpoolPost(Long id){

        Carpool carpool = findById(id);

        return new CarpoolResponse(
                carpool.getWriter().getNickname(), // 작성자
                carpool.getTitle(), // 제목
                carpool.getContent(), // 내용
                carpool.getMeetAt(), // 약속시간
                CarpoolResponse.getRouteFromCarpool(carpool), // Route
                CarpoolResponse.getMiddleSpotFromCarpool(carpool), // MiddleSpot
                carpool.getApplyCount(), // 지원자 수
                carpool.getMaxCount() // 가능 지원자 수
        );

    }



    // 해당 지역의 카풀 포스팅 가져오기
    public Page<CarpoolResponse> getLocalCarpools(Integer sigunguCode,
                                                  int page,
                                                  int size){

        Page<Carpool> result = carpoolRepository.findByWriter_SigunguCode(
                sigunguCode,
                PageRequest.of(page, size, Sort.by("postedAt").descending())
        );

        return result.map(carpool ->
            new CarpoolResponse(
                    carpool.getWriter().getNickname(), // 작성자
                    carpool.getTitle(), // 제목
                    carpool.getContent(), // 글 내용
                    carpool.getMeetAt(), // 약속시간
                    CarpoolResponse.getRouteFromCarpool(carpool), // Route
                    CarpoolResponse.getMiddleSpotFromCarpool(carpool), // MiddleSpot
                    carpool.getApplyCount(), // 지원자 수
                    carpool.getMaxCount() // 가능 지원자 수
            )
        );
    }

    // 자신의 카풀을 가져오는 메소드
    public MyCarpoolResponse getMyCarpools(Long writerId){

        List<Carpool> carpools = findByWriterId(writerId);

        List<MyCarpoolResponse.MyPost> myPosts = carpools
                .stream()
                .map(carpool ->
                        new MyCarpoolResponse.MyPost(
                                carpool.getId(),
                                carpool.getTitle(),
                                carpool.getPostedAt()))
                .toList();

        return new MyCarpoolResponse(myPosts);
    }


    /*
    스팟 추천관련
     */

    // 근처에 존재하는 하차 스팟 찾기
    public NearBySpot findNearBy (Double lat,
                      Double lng){

        NearByResponse nearByResponse = googleMapClient.searchNearby(lat,lng);

        return new NearBySpot(
                nearByResponse.places()
                        .stream()
                        .map(place -> new NearBySpot.Result(
                                place.location().latitude(),
                                place.location().longitude(),
                                place.displayName().text(),
                                mathHelper
                                        .haversineDistance(lat,lng,
                                                place.location().latitude(),
                                                place.location().longitude())
                        ))
                        .toList()
        );

    }

    // 추천지점 - 도착지점 사이의 30개 지점들의 해발고도를 반환하는 메소드
    public List<Double> getElevationNearBy
    (GetGradiantRequest getGradiantRequest){

        GetGradiantResponse getGradiantResponse = googleMapClient
                .getGradiantNearBy(getGradiantRequest.startLat(),
                        getGradiantRequest.startLng(),
                        getGradiantRequest.endLat(),
                        getGradiantRequest.endLng());

        return getGradiantResponse
                .results()
                .stream()
                .map(GetGradiantResponse.results::elevation)
                .toList();

    }

    // 인근 지점간의 경사율을 구하는 메소드
    public List<Double> getElevationsRateNearBy(List<Double> elevations,Double distance){

        return mathHelper
                .calculateElevationRate(mathHelper
                        .calculateSegmentHeightDifferences(elevations),distance);
    }

    // 경사 위험도를 구하는 메소드
    public Integer evaluateElevationRisk(List<Double> elevationRates){

        return elevationRates
                .stream()
                .mapToInt(rate ->{
                    if(rate<=5) return 0;
                    if((rate > 5)&&(rate <=8)) return 1;
                    if(rate>8) return 2;
                    return 2;
                })
                .sum();


    }

    // 거리 위험도를 구하는 메소드
    public Integer evaluateDistanceRisk(Double distance){
        return (distance.intValue())/10;
    }




}
