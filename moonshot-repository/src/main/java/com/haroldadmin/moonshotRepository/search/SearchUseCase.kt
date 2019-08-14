package com.haroldadmin.moonshotRepository.search

/**
 * All UseCases for searching are expected to inherit form this base class,
 * so that they can update the backing fields for their search query and limit dynamically
 */
abstract class SearchUseCase {
    var _query = ""
    var _limit = 10
}