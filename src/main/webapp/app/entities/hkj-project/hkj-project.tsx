import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { JhiItemCount, JhiPagination, TextFormat, Translate, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './hkj-project.reducer';

export const HkjProject = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const hkjProjectList = useAppSelector(state => state.hkjProject.entities);
  const loading = useAppSelector(state => state.hkjProject.loading);
  const totalItems = useAppSelector(state => state.hkjProject.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [pageLocation.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="hkj-project-heading" data-cy="HkjProjectHeading">
        <Translate contentKey="serverApp.hkjProject.home.title">Hkj Projects</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="serverApp.hkjProject.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/hkj-project/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="serverApp.hkjProject.home.createLabel">Create new Hkj Project</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {hkjProjectList && hkjProjectList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="serverApp.hkjProject.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('name')}>
                  <Translate contentKey="serverApp.hkjProject.name">Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('name')} />
                </th>
                <th className="hand" onClick={sort('coverImage')}>
                  <Translate contentKey="serverApp.hkjProject.coverImage">Cover Image</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('coverImage')} />
                </th>
                <th className="hand" onClick={sort('description')}>
                  <Translate contentKey="serverApp.hkjProject.description">Description</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('description')} />
                </th>
                <th className="hand" onClick={sort('startDate')}>
                  <Translate contentKey="serverApp.hkjProject.startDate">Start Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('startDate')} />
                </th>
                <th className="hand" onClick={sort('expectDate')}>
                  <Translate contentKey="serverApp.hkjProject.expectDate">Expect Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('expectDate')} />
                </th>
                <th className="hand" onClick={sort('endDate')}>
                  <Translate contentKey="serverApp.hkjProject.endDate">End Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('endDate')} />
                </th>
                <th className="hand" onClick={sort('status')}>
                  <Translate contentKey="serverApp.hkjProject.status">Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('status')} />
                </th>
                <th className="hand" onClick={sort('priority')}>
                  <Translate contentKey="serverApp.hkjProject.priority">Priority</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('priority')} />
                </th>
                <th className="hand" onClick={sort('actualCost')}>
                  <Translate contentKey="serverApp.hkjProject.actualCost">Actual Cost</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('actualCost')} />
                </th>
                <th className="hand" onClick={sort('qualityCheck')}>
                  <Translate contentKey="serverApp.hkjProject.qualityCheck">Quality Check</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('qualityCheck')} />
                </th>
                <th className="hand" onClick={sort('isDeleted')}>
                  <Translate contentKey="serverApp.hkjProject.isDeleted">Is Deleted</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('isDeleted')} />
                </th>
                <th className="hand" onClick={sort('createdBy')}>
                  <Translate contentKey="serverApp.hkjProject.createdBy">Created By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdBy')} />
                </th>
                <th className="hand" onClick={sort('createdDate')}>
                  <Translate contentKey="serverApp.hkjProject.createdDate">Created Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdDate')} />
                </th>
                <th className="hand" onClick={sort('lastModifiedBy')}>
                  <Translate contentKey="serverApp.hkjProject.lastModifiedBy">Last Modified By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedBy')} />
                </th>
                <th className="hand" onClick={sort('lastModifiedDate')}>
                  <Translate contentKey="serverApp.hkjProject.lastModifiedDate">Last Modified Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedDate')} />
                </th>
                <th>
                  <Translate contentKey="serverApp.hkjProject.manager">Manager</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="serverApp.hkjProject.category">Category</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="serverApp.hkjProject.material">Material</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {hkjProjectList.map((hkjProject, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/hkj-project/${hkjProject.id}`} color="link" size="sm">
                      {hkjProject.id}
                    </Button>
                  </td>
                  <td>{hkjProject.name}</td>
                  <td>{hkjProject.coverImage}</td>
                  <td>{hkjProject.description}</td>
                  <td>{hkjProject.startDate ? <TextFormat type="date" value={hkjProject.startDate} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>
                    {hkjProject.expectDate ? <TextFormat type="date" value={hkjProject.expectDate} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>{hkjProject.endDate ? <TextFormat type="date" value={hkjProject.endDate} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>
                    <Translate contentKey={`serverApp.HkjOrderStatus.${hkjProject.status}`} />
                  </td>
                  <td>
                    <Translate contentKey={`serverApp.HkjPriority.${hkjProject.priority}`} />
                  </td>
                  <td>{hkjProject.actualCost}</td>
                  <td>{hkjProject.qualityCheck ? 'true' : 'false'}</td>
                  <td>{hkjProject.isDeleted ? 'true' : 'false'}</td>
                  <td>{hkjProject.createdBy}</td>
                  <td>
                    {hkjProject.createdDate ? <TextFormat type="date" value={hkjProject.createdDate} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>{hkjProject.lastModifiedBy}</td>
                  <td>
                    {hkjProject.lastModifiedDate ? (
                      <TextFormat type="date" value={hkjProject.lastModifiedDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{hkjProject.manager ? <Link to={`/user-extra/${hkjProject.manager.id}`}>{hkjProject.manager.id}</Link> : ''}</td>
                  <td>{hkjProject.category ? <Link to={`/hkj-category/${hkjProject.category.id}`}>{hkjProject.category.id}</Link> : ''}</td>
                  <td>{hkjProject.material ? <Link to={`/hkj-material/${hkjProject.material.id}`}>{hkjProject.material.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/hkj-project/${hkjProject.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/hkj-project/${hkjProject.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() =>
                          (window.location.href = `/hkj-project/${hkjProject.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
                        }
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="serverApp.hkjProject.home.notFound">No Hkj Projects found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={hkjProjectList && hkjProjectList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default HkjProject;
