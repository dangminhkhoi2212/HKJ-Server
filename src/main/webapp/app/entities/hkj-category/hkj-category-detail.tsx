import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './hkj-category.reducer';

export const HkjCategoryDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const hkjCategoryEntity = useAppSelector(state => state.hkjCategory.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="hkjCategoryDetailsHeading">
          <Translate contentKey="serverApp.hkjCategory.detail.title">HkjCategory</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{hkjCategoryEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="serverApp.hkjCategory.name">Name</Translate>
            </span>
          </dt>
          <dd>{hkjCategoryEntity.name}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="serverApp.hkjCategory.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{hkjCategoryEntity.isDeleted ? 'true' : 'false'}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="serverApp.hkjCategory.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{hkjCategoryEntity.createdBy}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="serverApp.hkjCategory.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {hkjCategoryEntity.createdDate ? (
              <TextFormat value={hkjCategoryEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="serverApp.hkjCategory.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{hkjCategoryEntity.lastModifiedBy}</dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="serverApp.hkjCategory.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {hkjCategoryEntity.lastModifiedDate ? (
              <TextFormat value={hkjCategoryEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/hkj-category" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/hkj-category/${hkjCategoryEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default HkjCategoryDetail;
