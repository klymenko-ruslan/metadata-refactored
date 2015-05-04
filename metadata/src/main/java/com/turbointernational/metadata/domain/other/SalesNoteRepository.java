//package com.turbointernational.metadata.domain.other;
//
//import java.util.List;
//import org.springframework.data.repository.PagingAndSortingRepository;
//import org.springframework.data.repository.query.Param;
//import org.springframework.data.rest.core.annotation.RepositoryRestResource;
//
///**
// *
// * @author jrodriguez
// */
//@RepositoryRestResource(collectionResourceRel = "people", path = "people")
//public interface SalesNoteRepository extends PagingAndSortingRepository<SalesNote, Long> {
//    List<SalesNote> findByPartId(@Param("partId") Long partId);
//}